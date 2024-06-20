/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event;

import org.graalvm.polyglot.Value;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unchecked") // Important, Consumer has no type due to type conflicts with ? | Java, what is a "capture of ?"???
public class EventManager {
    private static EventManager INSTANCE;

    /**
     * Get the instance of EventManager, only one should ever exist
     * @return {@link EventManager}
     */
    public static EventManager get() {
        if (INSTANCE == null) {
            INSTANCE = new EventManager();
        }
        return INSTANCE;
    }
    private EventManager() {}

    private final Map<Class<? extends Event>, List<EventMethodData>> HANDLERS = new HashMap<>();
    private final Map<Class<? extends Event>, List<EventMethodData>> Z_HANDLERS = new HashMap<>();
    private final Map<Class<? extends Event>, List<Consumer>> CONSUMER_HANDLERS = new HashMap<>();
    private final Map<Class<? extends Event>, List<Value>> EXTERNAL_HANDLERS = new HashMap<>();

    /**
     * Fire an event
     * @param event The event to fire
     * @return If the event should continue
     */
    public boolean fireEvent(Event event) {
        List<EventMethodData> lEMD = HANDLERS.get(event.getClass());
        for (EventMethodData eMD : lEMD) {
            try {
                eMD.method().invoke(eMD.origin(), event);
            } catch (Exception e) {
                throw new RuntimeException("Error firing event!", e);
            }
        }

        List<EventMethodData> lEMD2 = Z_HANDLERS.get(event.getClass());
        for (EventMethodData eMD : lEMD2) {
            try {
                eMD.method().invoke(eMD.origin());
            } catch (Exception e) {
                throw new RuntimeException("Error firing event!", e);
            }
        }

        List<Value> lV = EXTERNAL_HANDLERS.get(event.getClass());
        for (Value v : lV) {
            v.execute(event);
        }

        List<Consumer> lC = CONSUMER_HANDLERS.get(event.getClass());
        for (Consumer c : lC) {
            c.accept(event);
        }

        if (event.getClass().isAssignableFrom(CancellableEvent.class)) {
            return !((CancellableEvent) event).cancelled();
        }

        if (event instanceof AfterEvent afterEvent) {
            afterEvent.afterEvent();
        }

        return true;
    }

    /**
     * Register an event
     * @param clazz The event class
     */
    public void registerEvent(Class<? extends Event> clazz) {
        HANDLERS.put(clazz, new ArrayList<>());
        Z_HANDLERS.put(clazz, new ArrayList<>());
        EXTERNAL_HANDLERS.put(clazz, new ArrayList<>());
        CONSUMER_HANDLERS.put(clazz, new ArrayList<>());
    }

    /**
     * Register many events at once
     * @param clazzes The event classes
     */
    public void registerEvents(List<Class<? extends Event>> clazzes) {
        for (Class<? extends Event> clazz : clazzes) {
            registerEvent(clazz);
        }
    }

    /**
     * Register a new listener, the class method
     * @param listenerClass The class to register
     */
    public void registerListener(Object listenerClass) {
        try {
            for (Method method : listenerClass.getClass().getMethods()) {
                if (method.isAnnotationPresent(EventListener.class)) {
                    if (method.getAnnotation(EventListener.class).event() != null && !method.getAnnotation(EventListener.class).event().equals(Event.class)) {
                        if (method.getParameters().length == 0) {
                            Z_HANDLERS.get(method.getAnnotation(EventListener.class).event()).add(new EventMethodData(method, listenerClass));
                        } else if (method.getParameters().length == 1) {
                            if (method.getParameters()[0].getType().isAssignableFrom(Event.class) && HANDLERS.get(method.getParameters()[0].getType()) != null) {
                                HANDLERS.get(method.getParameters()[0].getType()).add(new EventMethodData(method, listenerClass));
                            } else {
                                System.out.println("Invalid listener found!!! Ignoring...");
                            }
                        } else {
                            System.out.println("Invalid listener found!!! Ignoring...");
                        }
                    } else {
                        if (method.getParameters().length == 1)  {
                            if (method.getParameters()[0].getType().isAssignableFrom(Event.class) && HANDLERS.get(method.getParameters()[0].getType()) != null) {
                                HANDLERS.get(method.getParameters()[0].getType()).add(new EventMethodData(method, listenerClass));
                            } else {
                                System.out.println("Invalid listener found!!! Ignoring...");
                            }
                        } else {
                            System.out.println("Invalid listener found!!! Ignoring...");
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error registering new listener!", e);
        }
    }

    /**
     * Register a new listener, the JavaScript method
     * @param cls The event class to listen to
     * @param value The listener
     * @param <T> The event type
     */
    public <T extends Event> void registerListener(Class<T> cls, Value value) {
        if (value.canExecute()) {
            EXTERNAL_HANDLERS.get(cls).add(value);
        }
    }

    /**
     * Register a new listener, the Consumer method
     * @param cls The event class to listen to
     * @param consumer The listener
     * @param <T> The event type
     */
    public <T extends Event> void registerListener(Class<T> cls, Consumer<T> consumer) {
        CONSUMER_HANDLERS.get(cls).add(consumer);
    }

    /**
     * Unregisters a listener
     * @param listenerClass The class to unregister
     */
    public void unregisterListener(Object listenerClass) {
        for (Map.Entry<Class<? extends Event>, List<EventMethodData>> lEMD : HANDLERS.entrySet()) {
            for (EventMethodData eMD : lEMD.getValue()) {
                if (eMD.origin().equals(listenerClass)) {
                    lEMD.getValue().remove(eMD);
                    HANDLERS.put(lEMD.getKey(), lEMD.getValue());
                }
            }
        }
    }
}
