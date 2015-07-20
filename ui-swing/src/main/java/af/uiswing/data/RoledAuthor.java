/*
 * Unlicensed
 */
package af.uiswing.data;

import af.model.Author;

/**
 *
 * @author Alessandro Falappa <alex.falappa@gmail.com>
 */
public class RoledAuthor {

    Author author;
    AuthorRoles role;

    public RoledAuthor(Author author, AuthorRoles role) {
        this.author = author;
        this.role = role;
    }

    public Author getAuthor() {
        return author;
    }

    public AuthorRoles getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "RoledAuthor {" + author.getNameSurname() + ", " + role.toString() + "}";
    }

}
