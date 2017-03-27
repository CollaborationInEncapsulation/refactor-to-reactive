package com.example.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;

@Data
@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of", onConstructor = @__(@PersistenceConstructor))
public class User implements Persistable<String>, Serializable {
    @Id
    @NonNull
    private String id;
    @NonNull
    private String displayName;

    @Override
    public boolean isNew() {
        return true;
    }
}
