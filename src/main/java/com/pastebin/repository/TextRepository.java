package com.pastebin.repository;

import com.pastebin.model.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends JpaRepository<Text, Integer> {

    Text findByHashId(String hash);

}
