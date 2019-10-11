package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.model.note.Note;
import seedu.address.model.note.NoteList;
import seedu.address.model.person.Person;
import seedu.address.model.question.Question;
import seedu.address.model.question.QuestionList;
import seedu.address.model.student.Student;
import seedu.address.model.student.UniqueStudentList;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {

    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final QuestionList questions;
    private final NoteList notes;
    private final UserPrefs userPrefs;
    private final StudentRecord studentRecord;
    private final FilteredList<Person> filteredPersons;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyStudentRecord studentRecord,ReadOnlyUserPrefs userPrefs) {
        super();
        requireAllNonNull(addressBook, userPrefs);

        logger.fine(
            "Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.questions = new QuestionList();
        this.notes = new NoteList();
        this.userPrefs = new UserPrefs(userPrefs);
        this.studentRecord = new StudentRecord(studentRecord);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
    }

    public ModelManager() {
        this(new AddressBook(), new StudentRecord(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    //=========== StudentRecord ===========================================================================

//    @Override
//    public void addStudent(ReadOnlyStudentRecord studentRecord) {
//        this.studentRecord.resetData(student);
//    }
//
//    @Override
//    public Student deleteStudent(Index index) {
//        return students.remove(index);
//    }
//
//    @Override
//    public Student getStudent(Index index) {
//        return students.getStudent(index);
//    }
//
//    @Override
//    public void setStudent(Index index, Student student) {
//        students.setStudent(index, student);
//    }
//
//    @Override
//    public String getStudentList() {
//        return students.getStudentList();
//    }

    @Override
    public void setStudentRecord(ReadOnlyStudentRecord studentRecord) {
        this.studentRecord.resetData(studentRecord);
    }

    @Override
    public ReadOnlyStudentRecord getStudentRecord() {
        return studentRecord;
    }

    //=========== Students ================================================================================

    @Override
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return studentRecord.hasStudent(student);
    }

    @Override
    public void deleteStudent(Student target) {
        studentRecord.removeStudent(target);
    }

    @Override
    public void addStudent(Student student) {
        studentRecord.addStudent(student);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setStudent(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);
        studentRecord.setStudent(target, editedStudent);
    }

    //=========== Questions ================================================================================

    @Override
    public void addQuestion(Question question) {
        questions.addQuestion(question);
    }

    @Override
    public Question deleteQuestion(Index index) {
        return questions.deleteQuestion(index);
    }

    @Override
    public Question getQuestion(Index index) {
        return questions.getQuestion(index);
    }

    @Override
    public void setQuestion(Index index, Question question) {
        questions.setQuestion(index, question);
    }

    @Override
    public String getQuestionsSummary() {
        return questions.getQuestionsSummary();
    }

    //=========== Notes ================================================================================

    @Override
    public void addNote(Note note) {
        notes.addNote(note);
    }

    @Override
    public Note deleteNote(Index index) {
        return notes.deleteNote(index);
    }

    @Override
    public Note getNote(Index index) {
        return notes.getNote(index);
    }

    @Override
    public void setNote(Index index, Note question) {
        notes.setNote(index, question);
    }

    @Override
    public List<Note> getNotes() {
        return notes.getNotes();
    }

    @Override
    public String getNoteSummary() {
        return notes.getNoteSummary();
    }

    //=========== Person ================================================================================

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }

        // state check
        ModelManager other = (ModelManager) obj;
        return addressBook.equals(other.addressBook)
            && userPrefs.equals(other.userPrefs)
            && filteredPersons.equals(other.filteredPersons);
    }

}
