package com.deemo.hard.mapper;

import com.deemo.hard.entity.Game;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author qi_jiahu
 * @ClassName CaAdminMapper
 * @date 2019/6/26 17:25
 * @return
 */
@Mapper
public interface CaAdminMapper {

    List<Game> list();

    void update(Game game);
}
