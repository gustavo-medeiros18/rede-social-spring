package authproject.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User extends RepresentationModel<User> implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "username", nullable = false, length = 50)
  private String username;

  @Column(name = "password", nullable = false, length = 100)
  private String password;

  @Column(name = "email", nullable = false, length = 100)
  private String email;

  @Column(name = "enabled", nullable = false)
  private Boolean enabled = true;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Photo> photos;

  @CreationTimestamp(source = SourceType.DB)
  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @UpdateTimestamp(source = SourceType.DB)
  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public User() {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;
    return Objects.equals(id, user.id) &&
        Objects.equals(username, user.username) &&
        Objects.equals(password, user.password) &&
        Objects.equals(email, user.email) &&
        Objects.equals(enabled, user.enabled) &&
        Objects.equals(createdAt, user.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, email, enabled, createdAt);
  }
}
