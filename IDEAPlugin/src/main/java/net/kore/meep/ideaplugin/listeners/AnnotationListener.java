package net.kore.meep.ideaplugin.listeners;

import com.intellij.codeInspection.*;
import com.intellij.psi.*;
import net.kore.meep.api.event.EventListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotationListener implements InspectionSuppressor {
    @Override
    public boolean isSuppressedFor(@NotNull PsiElement element, @NotNull String toolId) {
        if (!toolId.equals("UnusedDeclaration")) {
            return false;
        }

        if (!(element instanceof PsiIdentifier)) {
            return false;
        }

        PsiElement parent = element.getParent();

        if (!(parent instanceof PsiMethod psiMethod)) {
            return false;
        }

        PsiAnnotation execute = psiMethod.getAnnotation(EventListener.class.getName());

        return execute != null;
    }

    @Override
    public SuppressQuickFix @NotNull [] getSuppressActions(@Nullable PsiElement element, @NotNull String toolId) {
        return SuppressQuickFix.EMPTY_ARRAY;
    }
}
