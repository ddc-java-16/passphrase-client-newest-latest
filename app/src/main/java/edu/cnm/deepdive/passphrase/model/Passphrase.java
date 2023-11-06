package edu.cnm.deepdive.passphrase.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a single passphrase, consisting of a name, a persistence key, and a list of words.
 */
public final class Passphrase {


  private static  final String TO_STRING_FORMAT = "%1$s[key=%2$s, name=%3$s, words=%4$s, length=%5$d]";

  @Expose(deserialize = true, serialize = false)
  @SerializedName("id")
  private final String key = null;
  @Expose(deserialize = true, serialize = false)
  private final Date created = null;
  @Expose(deserialize = true, serialize = false)
  private final Date modified = null;
  @Expose
  private String name;
  @Expose
  private List<String> words;
  @Expose(deserialize = false, serialize = true)
  private int length;         // TODO: 10/24/23 Create getter that checks if words field is null: if


  /**
   * Appends specified {@code word} to the end of the list of words in this passphrase.
   *
   * @param word {@link String} to append to passphrase.
   * @return {@code true} if {@code word} was successfully added, {@code false} otherwise.
   */
  public boolean append(String word) {
    ensureWordsExists();
    words.add(word);
    if(words.contains(word)) {
    return true;}
    else {
      return false;
    }

  }

  /**
   * Inserts specified {@code word} at the start of this passphrase.
   *
   * @param word {@link String} to insert in passphrase.
   * @return {@code true} if {@code word} was successfully inserted, {@code false} otherwise.
   */
  public boolean insert(String word) {
    ensureWordsExists();
    words.add(0, word);
    if (words.contains(word)) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Inserts specified {@code word} at index {@code position} in this passphrase.
   *
   * @param position index at which {@code word} is inserted in passphrase.
   * @param word     {@link String} to insert in passphrase.
   * @return {@code true} if {@code word} was successfully inserted, {@code false} otherwise.
   * @throws IndexOutOfBoundsException if {@code position} is negative or greater than passphrase
   *                                   length.
   */
  public boolean insert(int position, String word) throws IndexOutOfBoundsException {
    ensureWordsExists();
    isOutOfBounds(position);
    words.add(position, word);
    if (words.get(position) == word) {
      return true;
    } else {
      return false;
    }

  }

  /**
   * Removes word at specified {@code position} from this passphrase.
   *
   * @param position index from which to remove word from passphrase.
   * @return {@link String} word removed.
   * @throws IndexOutOfBoundsException if {@code position} is negative or greater than or equal to
   *                                   passphrase length.
   */
  public String remove(int position) throws IndexOutOfBoundsException {
    ensureWordsExists();
    isOutOfBounds(position);
    String wordremoved = words.get(position);
    words.remove(position);
    return wordremoved;


  }

  /**
   * Removes the first occurrence (if any) of specified {@code word} from this passphrase.
   *
   * @param word {@link String} to be removed from passphrase.
   * @return {@code true} if {@code word} was found and removed, {@code false} otherwise.
   */
  public boolean remove(String word) {
    words.remove(word);
    if(!words.contains(word)) {
      return true;
    } else {
      return false;
    }



  }

  /**
   * Removes all words from this passphrase.
   */
  public void clear() {
    ensureWordsExists();
    words.clear();
  }

  /**
   * Truncates list to specified {@code length}.
   *
   * @param length Size of truncated list.
   * @return {@code true} if list was successfully truncated, {@code false} otherwise.
   * @throws IndexOutOfBoundsException if {@code position} is negative or greater than or equal to
   *                                   passphrase length.
   */
  public boolean truncate(int length) throws IndexOutOfBoundsException {
    isOutOfBounds(length);
    words = words.subList(0, length);
return true;

  }

  /**
   * Gets (retrieves) the word at the specified position in this passphrase.
   *
   * @param position {@code int} index of word to retrieve.
   * @return {@link String} retrieved word.
   * @throws IndexOutOfBoundsException if {@code position} is negative or greater than or equal to
   *                                   passphrase length.
   */
  public String get(int position) throws IndexOutOfBoundsException {
    isOutOfBounds(position);
    return  words.get(position);
  }

  private void isOutOfBounds(int position) {
    if (position < 0 || position > words.size()) {
      throw new IndexOutOfBoundsException();
    }
  }

  /**
   * Sets (replaces) the word at the specified position in this passphrase.
   *
   * @param position {@code int} index of word to replace.
   * @param word     {@link String} new word to put at specified {@code position}.
   * @return {@link String} new word.
   * @throws IndexOutOfBoundsException if {@code position} is negative or greater than or equal to
   *                                   passphrase length.
   */
  public String set(int position, String word) throws IndexOutOfBoundsException {
    ensureWordsExists();
    isOutOfBounds(position);
words.set(position, word);
   return word;
  }

  @Override
  public int hashCode() {
    return (words==null) ? Objects.hash(name, key, words, length) : Objects.hash(name, key, words);

  }

  @Override
  public boolean equals(@Nullable Object obj) {

    boolean equivalent;
    if (obj == this) {
      equivalent = true;
    } else if (obj instanceof Passphrase other) {
      equivalent = Objects.equals(key, other.key) && Objects.equals(name, other.name)
    && (words != null && words.equals(other.words)
      || words == null && other.words == null && length == other.length);
    } else {
      equivalent = false;
    }
    return equivalent;
  }

  @NonNull
  @Override
  public String toString() {
   String className = getClass().getSimpleName();
   return (words == null)
       ? String.format(TO_STRING_FORMAT, className, key, name, words, length)
    : String.format(TO_STRING_FORMAT, className, key, name, words, words.size());

  }
  private synchronized void ensureWordsExists() {
    if (words == null) {
      words = new ArrayList<>();
    }
  }

  public String getKey() {
    return key;
  }

  public List<String> getWords() {
    return words;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public Date getCreated() {
    return created;
  }

  public Date getModified() {
    return modified;
  }
}
