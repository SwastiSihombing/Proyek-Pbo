package mapper;

import java.util.List;

/**
 * Generic interface untuk semua mapper (Repository pattern)
 * Mengimplementasikan basic CRUD operations
 */
public interface IMapper<T> {
    /**
     * Insert entity baru ke database
     * @return ID dari entity yang baru dibuat
     */
    int insert(T entity);

    /**
     * Find entity by primary key
     */
    T findById(int id);

    /**
     * Find semua entities
     */
    List<T> findAll();

    /**
     * Update entity
     */
    void update(T entity);

    /**
     * Delete entity
     */
    void delete(int id);

    /**
     * Check apakah entity exist
     */
    boolean exists(int id);
}
