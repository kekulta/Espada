# Espada - A Dependency Injection Experiment

Espada is a project born out of the desire to better understand Dependency Injection (DI). While it's important to note that Espada is primarily an educational experiment, it is meticulously documented to assist in comprehending DI concepts.
## Project Modules

1. `processor`

   - The core module containing annotation processing and code generation logic.
   - Extensively documented with comments for every class to aid understanding.
   - Comments serve as a learning resource and reference guide for those seeking insights into the project.

2. `annotations`

   - Provides the `@Module` annotation and `javax.inject` API for DI users.

3. `kotlin-poet-extensions`

   - A compact DSL using Kotlin extension functions to enhance the creation of files within the Kotlin Poet.

4. `app`

   - A small Android module featuring examples of using mocked dependencies. 
   - Includes generated factories and few comments for better understanding.

## Code Comments

Espada's codebase is richly annotated with comments, especially in the `processor` module, making it a valuable resource for learning about DI.
## Integration

To integrate Espada into your project:

1. Add the following dependencies:
    - Use `implementation()` for the `annotations` module.
    - Use `ksp()` for the `processor` module.
   ### example: 
      ```
         dependencies {
             ksp(project(":processor"))
             implementation(project(":annotations"))
         }
      ```
2. Apply the KSP (Kotlin Symbol Processing) plugin.
## Usage

To use Espada:

1. Declare your dependencies in any number of `@Module` annotated classes.

2. Mark the properties you want to inject with `@Inject.`

3. Espada will automatically generate factories, validate the dependency graph, and, if everything is in order, generate Injector classes with methods `inject(recipient)` that facilitate member injection into your classes.

> For explanations with examples you can look inside the `app` module

Espada is an educational experiment designed to foster a deeper understanding of DI concepts. Enjoy exploring and learning with Espada!
