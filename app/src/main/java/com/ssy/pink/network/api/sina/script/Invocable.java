package com.ssy.pink.network.api.sina.script;


/**
 * @author ssy
 * @date 2018/10/25
 */
public interface Invocable {
    Object invokeMethod(Object var1, String var2, Object... var3) throws ScriptException, NoSuchMethodException;

    Object invokeFunction(String var1, Object... var2) throws ScriptException, NoSuchMethodException;

    <T> T getInterface(Class<T> var1);

    <T> T getInterface(Object var1, Class<T> var2);
}
