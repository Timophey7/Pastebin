package com.pastebin.repository;

import com.pastebin.model.Views;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewsRepository extends CrudRepository<Views, String> {

    Views findByHashId(String hash);

}
