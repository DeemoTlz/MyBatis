package com.deemo.hard.service;

import com.deemo.hard.entity.Game;

public interface IGameService {
    Game getGameById(Integer id);

    void updateGame(Game game);
}
