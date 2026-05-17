import com.lemonappdev.konsist.api.Konsist
import org.junit.Assert.assertTrue
import org.junit.Test

class ArchitectureTest {

    @Test
    fun `domain classes should not depend on Android framework`() {
        val violations = Konsist.scopeFromProject()
            .classes()
            .filter { it.resideInPackage("..domain..") }
            .filter { clazz ->
                clazz.containingFile.imports.any { imp -> imp.name.startsWith("android.") }
            }

        assertTrue(
            "Domain classes with Android imports: ${violations.map { it.name }}",
            violations.isEmpty()
        )
    }

    @Test
    fun `data classes should not depend on UI components`() {
        val violations = Konsist.scopeFromProject()
            .classes()
            .filter { it.resideInPackage("..data..") }
            .filter { clazz ->
                clazz.containingFile.imports.any { imp ->
                    imp.name.contains("Fragment") ||
                    imp.name.contains("Activity") ||
                    imp.name.contains("ViewModel")
                }
            }

        assertTrue(
            "Data classes with UI imports: ${violations.map { it.name }}",
            violations.isEmpty()
        )
    }

    @Test
    fun `feature habits module should not depend on stats module`() {
        val violations = Konsist.scopeFromProject()
            .files
            .filter { it.packagee?.name?.startsWith("com.example.habits") == true }
            .filter { file -> file.imports.any { imp -> imp.name.startsWith("com.example.stats") } }

        assertTrue(
            "Habits files depending on stats: ${violations.map { it.name }}",
            violations.isEmpty()
        )
    }

    @Test
    fun `use cases should reside in domain layer`() {
        val violations = Konsist.scopeFromProject()
            .classes()
            .filter { it.name.endsWith("UseCase") }
            .filter { !it.resideInPackage("..domain..") }

        assertTrue(
            "UseCase classes outside domain: ${violations.map { it.name }}",
            violations.isEmpty()
        )
    }

    @Test
    fun `repository interfaces should be in domain and implementations in data`() {
        val interfaceViolations = Konsist.scopeFromProject()
            .interfaces()
            .filter { it.name.startsWith("I") && it.name.endsWith("Repository") }
            .filter { !it.resideInPackage("..domain..") }

        val implViolations = Konsist.scopeFromProject()
            .classes()
            .filter { it.name.endsWith("RepositoryImpl") }
            .filter { !it.resideInPackage("..data..") }

        assertTrue(
            "Repository interfaces outside domain: ${interfaceViolations.map { it.name }}",
            interfaceViolations.isEmpty()
        )
        assertTrue(
            "RepositoryImpl classes outside data: ${implViolations.map { it.name }}",
            implViolations.isEmpty()
        )
    }
}
