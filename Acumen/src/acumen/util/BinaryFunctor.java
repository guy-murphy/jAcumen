package acumen.util;

public interface BinaryFunctor<R,T1,T2> {
	public R invoke(T1 arg1, T2 arg2);
}
