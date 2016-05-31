package memozier;

/**
 * Title: PACKAGE_NAME<br>
 * Description: <br>
 * Copyright: Copyright (c) 2016<br>
 *
 * @author lili 2016/5/31
 */
public interface Computable<T, R> {
    public R convert(final T args);
}
