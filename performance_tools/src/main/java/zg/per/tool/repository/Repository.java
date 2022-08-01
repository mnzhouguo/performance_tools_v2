package zg.per.tool.repository;

import java.util.List;

public interface Repository<T> {
    List<T> findAll() throws Exception;
}
