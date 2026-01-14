package net.gopa.mc.whooshwhoosh.registry;

public interface ModRegistry<T>  {

    public void registerAll();

    public T get();
}
