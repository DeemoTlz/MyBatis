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
        gameMapper.update(game);
        System.out.println("===================");
        game.setDesc(game.getDesc() + "123456");
        gameMapper.update(game);
        // 测试事务
        // int i = 1 / 0;
    }
}
