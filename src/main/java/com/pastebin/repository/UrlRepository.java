package com.pastebin.repository;

import com.pastebin.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository<Url, Integer> {

    void deleteByUrl(String url);

}
