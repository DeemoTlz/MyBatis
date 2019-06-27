package com.deemo.hard.service;

import com.deemo.hard.entity.Game;

public interface IGameService {
    Game getGameById(Integer id);

    boolean updateGame(Game game);
}
