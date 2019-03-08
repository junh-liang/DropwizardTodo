package com.junhong.liang.todo.resources;

import com.junhong.liang.todo.api.Todo;
import com.junhong.liang.todo.api.TodoService;
import com.junhong.liang.todo.api.TodoServiceInMemoryImpl;
import com.junhong.liang.todo.api.TodoServiceMongoImpl;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;

import static org.junit.Assert.*;

import java.util.*;

@RunWith(Parameterized.class)
public class TodoResourceEndpointTest {

    TodoService service;

    @Rule
    public ResourceTestRule resources;

    public TodoResourceEndpointTest(String storageType) throws Exception {

        if (storageType.equals("memory")) service = new TodoServiceInMemoryImpl();
        else {
            DB db = new MongoClient("localhost", 27017).getDB("mongoTest");
            service = new TodoServiceMongoImpl(db);
        }

        resources = ResourceTestRule.builder()
                .addResource(new TodoResource(service))
                .build();
    }

    @Parameterized.Parameters(name = "backend storage: {0}")
    public static Collection services() {

        return Arrays.asList(new Object[][]{
                {"memory"},
                {"mongodb"}
        });
    }


    @Before
    public void beforeEachTest() {
        service.clearAll();
    }

    @After
    public void afterEachTest() {
        service.clearAll();
    }

    @Test
    public void testPostAndGetTodo() {
        Todo td = new Todo("Name 1", "Description 1", new ArrayList<>());
        Todo createdTodo = addTodo(td);

        Todo r = getSingleTodo(createdTodo.getId());

        assertTodoEqual(r, createdTodo);
    }

    @Test
    public void testGetAllTodos() {
        Todo td1 = new Todo("Name 1", "Description 1", new ArrayList<>());
        Todo td2 = new Todo("Name 2", "Description 2", new ArrayList<>());
        Todo td3 = new Todo("Name 3", "Description 3", new ArrayList<>());

        Map<String, Todo> expectedMap = new HashMap<>();
        List<Todo> list = new ArrayList<>();

        list.add(addTodo(td1));
        list.add(addTodo(td2));
        list.add(addTodo(td3));

        for (Todo td : list) {
            expectedMap.put(td.getId(), td);
        }

        List<Todo> r = resources.target("/todos").request().get(new GenericType<List<Todo>>() {
        });

        Map<String, Todo> resultMap = new HashMap<>();

        for (Todo td : r) {
            resultMap.put(td.getId(), td);
        }

        // Verify
        for (String k : resultMap.keySet()) {
            assertTodoEqual(expectedMap.get(k), resultMap.get(k));
        }
    }

    @Test
    public void testDelete() {
        // Add a new to-do item
        String todoId = addTodo(new Todo("Name 1", "Description 1", new ArrayList<>())).getId();

        // Make sure the to-do item exists
        List<Todo> r = resources.target("/todos").request().get(new GenericType<List<Todo>>() {
        });
        Map<String, Todo> initialMap = new HashMap<>();
        for (Todo td : r) {
            initialMap.put(td.getId(), td);
        }
        assertTrue(initialMap.containsKey(todoId));

        // Delete the to-do item
        resources.target("/todos/" + todoId).request().delete();

        // Check if the to-do item has been gone
        r = resources.target("/todos").request().get(new GenericType<List<Todo>>() {
        });
        Map<String, Todo> resultMap = new HashMap<>();
        for (Todo td : r) {
            resultMap.put(td.getId(), td);
        }
        assertFalse(resultMap.containsKey(todoId));
    }

    @Test
    public void testPut() {

        String todoId = addTodo(new Todo("Name 1", "Description 1", new ArrayList<>())).getId();
        Todo newTodo = new Todo(todoId, "Name 2", "Description 2", new ArrayList<>());

        resources.target("/todos/" + todoId).request().put(Entity.json(newTodo));

        Todo r = getSingleTodo(todoId);
        assertTodoEqual(newTodo, r, false);
    }

    @Test
    public void testMalformedPost() {

        Todo td = new Todo(null, "Description 1", new ArrayList<>());

        int statusCode = resources.target("/todos")
                .request()
                .post(Entity.json(td))
                .getStatus();

        assertEquals(422, statusCode);
    }


    // Helper method to test equalness of to-do item
    private void assertTodoEqual(Todo expected, Todo actual) {
        assertTodoEqual(expected, actual, true);
    }

    private void assertTodoEqual(Todo expected, Todo actual, boolean compareId) {
        if (compareId) assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getTasks().size(), actual.getTasks().size());
        // TODO: compare tasks in the list are the same
    }

    private Todo addTodo(Todo todo) {
        return resources.target("/todos")
                .request()
                .post(Entity.json(todo))
                .readEntity(Todo.class);
    }

    private Todo getSingleTodo(String todoId) {
        return resources.target("/todos/" + todoId).request().get(Todo.class);
    }

}
