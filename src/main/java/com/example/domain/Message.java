package com.example.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.domain.Persistable;

@Data
@Builder
@Entity
@Table(name = "Messages")
@NoArgsConstructor
@AllArgsConstructor(staticName = "of", onConstructor = @__(@PersistenceConstructor))
public class Message implements Persistable<String>, Serializable {

    @Id
    @NonNull
    private String id;
    @NonNull
    private String text;
    @NonNull
    private String html;
    @NonNull
    private Date sent;
    @NonNull
    private String userId;
    @NonNull
    private Long readBy;

    @Override
    public boolean isNew() {
        return true;
    }
}
