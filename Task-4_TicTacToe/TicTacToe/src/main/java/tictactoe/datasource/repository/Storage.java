package tictactoe.datasource.repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import tictactoe.datasource.model.GameEntity;

public class Storage {
    private final ConcurrentHashMap<UUID, GameEntity> entities;
    
    public Storage () {
        this.entities = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<UUID, GameEntity> entities() {
        return this.entities;
    }
}