package Application.Database;

public interface DBRepository<T> {
    Iterable<T> findAll();

    T findById(Long id);

    T deleteById(Long id);

    T save(T object);
}
