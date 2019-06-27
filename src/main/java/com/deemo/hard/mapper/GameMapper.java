package com.deemo.hard.mapper;

import com.deemo.hard.entity.Game;

import java.util.List;

/**
 * @author qi_jiahu
 * @ClassName CaAdminMapper
 * @date 2019/6/26 17:25
 * @return
 */
// @Mapper
public interface GameMapper {

    List<Game> list();

    void update(Game game);
}
