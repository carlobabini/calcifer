package it.angelobabini.calcifer.backend;

public interface CRUDLogic<T> {
	public T newEntity();
	public void deleteEntity(T t);
	public void saveEntity(T t);
	public void editEntity(T t);
}
