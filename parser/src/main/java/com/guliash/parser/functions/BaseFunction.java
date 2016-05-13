package com.guliash.parser.functions;

public abstract class BaseFunction<T, R> {

    public String name;

    public BaseFunction(String name) {
        this.name = name;
    }

    public abstract R calc(T...args);

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof BaseFunction) && this.name.equals(((BaseFunction)o).name);
    }
}
