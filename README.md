# MyBatis
Albrus learn how MyBatis works…φ(๑˃∀˂๑)♪

SqlSessionTemplate：
```java
// 开启事务后，初始化启动：
DataSourceTransactionManager.doBegin() {
    // ...省略
    if (txObject.isNewConnectionHolder()) {
        // TransactionSynchronizationManager.bindResource(obtainDataSource(), txObject.getConnectionHolder()):
        TransactionSynchronizationManager.bindResource() {
            // private static final ThreadLocal<Map<Object, Object>> resources = new NamedThreadLocal<>("Transactional resources");

            // ...省略
            Map<Object, Object> map = resources.get();
            // set ThreadLocal Map if none found
            if (map == null) {
                map = new HashMap<>();
                resources.set(map);
            }
            // 此时actualKey为连接池对象
            Object oldValue = map.put(actualKey, value);
        }
    }
}

// 当系统初始化启动时，会扫描mapper接口并创建 MapperProxy 对象：
// 此时会为每个mapper接口对象创建一个mapper代理对象（即 MapperProxy ：每个代理对象自身包含一个 SqlSession 对象（其实是 SqlSessionTemplate 对象，该对象中包含一个 SqlSessionFactory 对象））
public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
    this.sqlSession = sqlSession;
    this.mapperInterface = mapperInterface;
    this.methodCache = methodCache;
}

// 调用mapper接口的方法时，会携带该代理对象自带的 SqlSession 对象（其实是 SqlSessionTemplate 对象，该对象中包含一个 SqlSessionFactory 对象）
MapperMethod.execute(SqlSession, Object[]) {
    case UPDATE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.update(command.getName(), param));
        break;
    }
}

// sqlSession.update(command.getName(), param)，此 sqlSession 其实是 SqlSessionTemplate 对象
// TODO 此处是 sqlSessionProxy 动态代理接管，待深入学习代理后再详细写原理
// ~~断点调试时，若调试进入这一步，会自动触发调用 SqlSessionUtils.getSqlSession(SqlSessionFactory, ExecutorType, PersistenceExceptionTranslator)，~~
// ~~此时无 holder， 会通过 sessionFactory 创建一个真正的 SqlSession，若开启了事务，会将 SqlSession 存放到 TransactionSynchronizationManager.resources() 中~~
SqlSessionTemplate.update(String, Object) {
    // 调用真正 sqlSession代理对象
    return this.sqlSessionProxy.update(statement, parameter);
}

// 接上部分
// sqlSession代理对象，内部类：
SqlSessionTemplate.SqlSessionInterceptor.invoke() {
    SqlSession sqlSession = getSqlSession(
        // SqlSessionTemplate。this：调用者的 SqlSessionTemplate 对象
        // 开启事务时，sqlSessionFactory 后续作为 resources（ThreadLocal）的 key 获取 holder
        SqlSessionTemplate.this.sqlSessionFactory,
        SqlSessionTemplate.this.executorType,
        SqlSessionTemplate.this.exceptionTranslator);
}

SqlSessionUtils.getSqlSession(SqlSessionFactory, ExecutorType, PersistenceExceptionTranslator) {
    // 从 resources（ThreadLocal） 中获取holder，key 为 sessionFactory
    SqlSessionHolder holder = (SqlSessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
    
    SqlSession session = sessionHolder(executorType, holder);
    if (session != null) {
      return session;
    }

    // 若 session 为null，new 一个 DefaultSqlSession 对象
    LOGGER.debug(() -> "Creating a new SqlSession");
    session = sessionFactory.openSession(executorType);

    // 若开启事务，将 session 存放到 resources（ThreadLocal），key 为 sessionFactory
    registerSessionHolder(sessionFactory, executorType, exceptionTranslator, session);
}

```

一级缓存：
针对同一个sqlSession
```java
当未开启事务时、或开启事务但未执行更新操作时，一级缓存生效：
org.apache.ibatis.executor.BaseExecutor.localCache(PerpetualCache) 

BaseExecutor.query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql) {
    // ...省略
    
    // 先从一级缓存中获取，key 与 mapper 相关
    list = resultHandler == null ? (List<E>) localCache.getObject(key) : null;
    if (list != null) {
        handleLocallyCachedOutputParameters(ms, key, parameter, boundSql);
    } else {
        // 缓存中获取不到，查询数据库
        list = queryFromDatabase(ms, parameter, rowBounds, resultHandler, key, boundSql);
        /*List<E> BaseExecutor.queryFromDatabase(MappedStatement, Object parameter, RowBounds, ResultHandler, CacheKey key, BoundSql) {
            List<E> list;
            localCache.putObject(key, EXECUTION_PLACEHOLDER);
            try {
                list = doQuery(ms, parameter, rowBounds, resultHandler, boundSql);
            } finally {
                localCache.removeObject(key);
            }
            // 将查询数据放入缓存中
            localCache.putObject(key, list);
            if (ms.getStatementType() == StatementType.CALLABLE) {
                localOutputParameterCache.putObject(key, parameter);
            }
            return list;
        }*/
    }
}

当开启事务时：
在每次更新操作后，会清空缓存：
BaseExecutor.update(MappedStatement, Object parameter) {
    // ...省略
    
    clearLocalCache();
    /*@Override
    public void clearLocalCache() {
        if (!closed) {
          localCache.clear();
          localOutputParameterCache.clear();
        }
    }*/
}
```
