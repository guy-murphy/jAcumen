package acumen.util;

public interface UnaryFunctor<R,T> {
	public R invoke(T arg);
}
