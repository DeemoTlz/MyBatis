package com.deemo.hard.service.impl;

import com.deemo.hard.entity.Game;
import com.deemo.hard.mapper.GameMapper;
import com.deemo.hard.service.IGameService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation= Propagation.REQUIRED , isolation = Isolation.DEFAULT)
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
    public boolean updateGame(Game game) {
        gameMapper.update(game);
        // int i = 1 / 0;

        return true;
    }
}
