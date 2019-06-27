package com.deemo.test;

import com.deemo.hard.entity.Game;
import com.deemo.hard.mapper.GameMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MybatisTest {

    static private SqlSessionFactory sqlSessionFactory = null;
    static private SqlSession sqlSession = null;

    @Before
    public void getConnection() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void updateGame() {
        Game game = new Game();
        game.setId(2);
        game.setDesc("波兰蠢驴7777");

        /*CaAdminMapper mapper = sqlSession.getMapper(CaAdminMapper.class);
        mapper.update(game);
        System.out.println("============");*/

        // DefaultSqlSession 需要手动提交事务
        sqlSession.update("update", game);
        // sqlSession.commit();
    }

    @Test
    public void getGames() {

        List<Game> games = sqlSession.selectList("com.deemo.hard.mapper.CaAdminMapper.list");

        System.out.println(games.size());

        GameMapper mapper = sqlSession.getMapper(GameMapper.class);

        System.out.println(mapper.list().size());
    }

    @Test
    public void testSqlSession() {
        SqlSession sqlSession2 = sqlSessionFactory.openSession();

        System.out.println(sqlSession);
        System.out.println(sqlSession2);

        sqlSession2.close();
    }

    @After
    public void closeConnection() {
        if (null != sqlSession) {
            sqlSession.close();
        }
    }

}
