/*
 * Copyright (c) 2024. Kore Team. This project is licensed under the GPL-3.0 license.
 * You may find a copy here https://www.gnu.org/licenses/gpl-3.0.en.html
 */

package net.kore.meep.api.event;

import java.lang.reflect.Method;

/**
 * The data for an event listener
 * @param method The method to run
 * @param origin The instance of the listener
 */
public record EventMethodData(Method method, Object origin) {
}
