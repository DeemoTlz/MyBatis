package com.deemo.test;

import com.deemo.hard.entity.Game;
import com.deemo.hard.service.IGameService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringTest {

    private IGameService iGameService = null;

    @Before
    public void init() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println(applicationContext);

        iGameService = applicationContext.getBean(IGameService.class);
        System.out.println(iGameService);
    }

    @Test
    public void updateTest() {

        Game game = new Game();
        game.setId(2);
        game.setDesc("波兰蠢驴666");

        iGameService.updateGame(game);
    }

    @Test
    public void getTest() {

        Game game = iGameService.getGameById(1);

        System.out.println(game.getName());

    }
}
