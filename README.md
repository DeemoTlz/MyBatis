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

// 当系统初始化启动时，会扫描mapper接口并创建MapperProxy对象：
// 此时会为每个mapper接口对象创建一个mapper代理对象（每个代理对象自身包含一个SqlSession（SqlSessionFactory）对象）
public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
    this.sqlSession = sqlSession;
    this.mapperInterface = mapperInterface;
    this.methodCache = methodCache;
}

// 调用mapper接口的方法时，会携带该代理对象自带的 SqlSession（SqlSessionFactory）对象
MapperMethod.execute(SqlSession, Object[]) {
    case UPDATE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.update(command.getName(), param));
        break;
    }
}

// sqlSession.update(command.getName(), param)，此sqlSession其实是 SqlSessionFactory 对象
// 调用这一步时，会将 sqlSession 自动存放到TransactionSynchronizationManager.resources() 中，why？
SqlSessionTemplate.update(String, Object) {
    // 调用真正 sqlSession代理对象
    return this.sqlSessionProxy.update(statement, parameter);
}

// 接上部分
// sqlSession代理对象：
SqlSessionTemplate.SqlSessionInterceptor.invoke() {
    SqlSession sqlSession = getSqlSession(
              // 调用者的sqlSessionFactory对象，后续作为resources（ThreadLocal）的key获取holder
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

    // 若session为null，new 一个 DefaultSqlSession（当线程通过mapper进入时，MapperProxy会直接创建一个session）
    LOGGER.debug(() -> "Creating a new SqlSession");
    session = sessionFactory.openSession(executorType);

    // 将 session 存放到 resources（ThreadLocal），key 为 sessionFactory
    registerSessionHolder(sessionFactory, executorType, exceptionTranslator, session);
}

```