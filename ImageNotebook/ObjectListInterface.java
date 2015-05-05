public interface ObjectListInterface
{
  Object  getCurrent();
  Object  getFirst();
  Object  getNext();
  Object  getPrevious();
  Object  getLast();
  boolean append(Object newObject);
  boolean insert(Object newObject);
  Object  remove();
  boolean replace(Object newObject);
  boolean clear();
  int     getLength();
  int     getCurrentPosition();
}
