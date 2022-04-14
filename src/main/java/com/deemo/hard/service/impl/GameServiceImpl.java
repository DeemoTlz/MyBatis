package com.deemo.hard.service.impl;

import com.deemo.hard.entity.Game;
import com.deemo.hard.mapper.GameMapper;
import com.deemo.hard.service.IGameService;
import org.springframework.stereotype.Service;

@Service
// 注解或applicationContext.xml中开启配置
// @Transactional(propagation= Propagation.REQUIRED , isolation = Isolation.DEFAULT)
public class GameServiceImpl implements IGameService {

    private final GameMapper gameMapper;

    public GameServiceImpl(GameMapper gameMapper) {
        this.gameMapper = gameMapper;
    }

    @Override
    public Game getGameById(Integer id) {
        return gameMapper.list().get(0);
    }

    @Override
    public void updateGame(Game game) {
        System.out.println(gameMapper.get(game.getId()));
        System.out.println(gameMapper.get(game.getId()));
        // gameMapper.update(game);
        game.setDescription(game.getDescription() + "123456");
        // 更新后会导致一级缓存失效
        gameMapper.update(game);
        // 测试事务
        // int i = 1 / 0;
        System.out.println("========================================================");
        System.out.println(gameMapper.get(game.getId()));

        /*new Thread(() -> {
            System.out.println("========================================================");
            System.out.println(gameMapper.get(game.getId()));
            Game newGame = new Game();
            newGame.setName("Dark Soul");
            newGame.setPrice(299.0);
            newGame.setDesc("有一天");

            gameMapper.insert(newGame);
        }).start();*/
    }
}
