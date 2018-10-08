package com.example.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("Mentions")
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
