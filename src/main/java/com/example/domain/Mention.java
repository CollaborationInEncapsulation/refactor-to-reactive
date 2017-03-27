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
@Table(name = "Mentions")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of", onConstructor = @__(@PersistenceConstructor))
public class Mention implements Persistable<String>, Serializable {

    @NonNull
    @Id
    private String id;

    @NonNull
    private String userId;

    @NonNull
    private String messageId;

    @Override
    public boolean isNew() {
        return true;
    }
}
