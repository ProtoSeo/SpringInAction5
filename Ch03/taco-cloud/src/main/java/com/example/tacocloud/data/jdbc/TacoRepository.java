package com.example.tacocloud.data.jdbc;

import com.example.tacocloud.Taco;

public interface TacoRepository {
    Taco save(Taco design);
}
