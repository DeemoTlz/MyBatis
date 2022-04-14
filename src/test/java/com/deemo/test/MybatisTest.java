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
    public void testSqlSession() {
        SqlSession sqlSession2 = sqlSessionFactory.openSession();

        System.out.println(sqlSession);
        System.out.println(sqlSession2);

        sqlSession2.close();
    }

    @Test
    public void getGames() {
        GameMapper mapper = sqlSession.getMapper(GameMapper.class);

        Game entity = mapper.get(1);
        System.out.println("ID: " + entity);

        List<Game> games1 = mapper.list();
        System.out.println("Count: " + games1.size());

        List<Game> games2 = sqlSession.selectList("com.deemo.hard.mapper.GameMapper.list");
        System.out.println("Count: " + games2.size());

        Game game = new Game();
        game.setName("Deemo");
        game.setPrice(32.0);
        game.setDescription("");
        Integer insert = mapper.insert(game);
        sqlSession.commit();
        System.out.println("The insert id: " + insert);
    }

    @Test
    public void updateGame() {
        Game game = new Game();
        game.setId(2);
        game.setDescription("波兰蠢驴7777");

        /*GameMapper mapper = sqlSession.getMapper(GameMapper.class);
        mapper.update(game);
        System.out.println("============");*/

        // DefaultSqlSession 需要手动提交事务
        sqlSession.update("update", game);
        // sqlSession.commit();
    }

    @Test
    public void addGames() {
        Game game = new Game();
        game.setName("赛博朋克 2077");
        game.setPrice(178.0);
        game.setDescription("波兰蠢驴7777");

        GameMapper mapper = sqlSession.getMapper(GameMapper.class);
        mapper.insert(game);
        sqlSession.commit();

        System.out.println(game);
    }

    @After
    public void closeConnection() {
        if (null != sqlSession) {
            sqlSession.close();
        }
    }

}
