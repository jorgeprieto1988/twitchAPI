# PARTE TEORICA

### Arquitecturas de UI: MVP, MVI y MVVM

#### MVP

##### ¿En qué consiste esta arquitectura?
El Model View Presenter se basa en algo parecido al MVC, ya que consiste en separar la UI de los datos dividiendose así en tres partes
Model: Aquí se define la lógica de negocio, donde se manejan los datos
View: Muestra la UI y escucha las interacciones con el usuario, la representación de esto sería una Activity o Fragment.
Presenter: Es la parte que se encarga de comunicar el Model con el View y se encarga de la lógica de presentación.

Esta arquitectura comienza su flujo desde la View, comunicandose con el Presenter cuando hay una interacción con el usuario, y el Presenter a su vez se
comunicará con el Model el cual volverá a contestar a Presenter y éste a View. 

##### ¿Cuáles son sus ventajas?
Al estar la vista y los datos divididos el testeo se hace mucho mas sencillo.


##### ¿Qué inconvenientes tiene?
Para conseguir la separación total para facilitar el testing, se necesita generar interface junto con el view y el presenter
lo que puede dificultar un poco la tarea a llevar.
También se necesita añadir logica adicional para el Presenter a la hora de destruirlo o no cuando se destruye la activity con la que se usa.

#### MVI

##### ¿En qué consiste esta arquitectura?
El Model-View-Intent funciona diferente a MVP o MVC, consiste en:
Model: Representa un estado y deberían ser inmutables, ya que deben asegurar el flujo de datos unidireccional entre Model y las otras capas de la arquitectura.
Intent: Representa un intento de una acción que el usuario quiere hacer. Por cada acción del usuario un Intent llegara desde la View y 
será procesado y se traducirá en un nuevo estado en el Model.
View: Como en el MVP, son representados por interfaces que se implementan en la Activity or Fragment.

Lo excepcional de esta arquitectura es el Model actuando también como un manejador de estados que cambiará según las acciones del usuario.
Como el modelo debería ser inmutable, cada vez que hay una interacción en el View, se tenga que generar un nuevo Model con estado diferente,
esto genera que flujo ciclico donde View comunica a Intent, éste a Model, y Model (nuevo model con nuevo estado) se comunica
con View.

##### ¿Cuáles son sus ventajas?
Un flujo de datos unidireccional y cíclico que mantener.
Estado consistente durante el ciclo de vida de las Views.
Model inmutable que proporcionan un comportamiento fiable y thread-safe.


##### ¿Qué inconvenientes tiene?
Se requiere tiempo para el aprendizaje ya que esta arquitectura viene de otras tecnologías como programación reactiva y multi-threading.

#### MVVM

##### ¿En qué consiste esta arquitectura?
Escribe tu respuesta aquí

##### ¿Cuáles son sus ventajas?
Escribe tu respuesta aquí

##### ¿Qué inconvenientes tiene?
Escribe aquí tu respuesta

---

### Testing

#### ¿Qué tipo de tests se deberían incluir en cada parte de la pirámide de test? Pon ejemplos de librerías de testing para cada una de las partes.
Escribe aquí tu respuesta

#### ¿Por qué los desarrolladores deben centrarse sobre todo en los Unit Tests?
Escribe aquí tu respuesta

---

### Inyección de dependencias

#### ¿En qué consiste la inyección de dependencias y por qué nos ayuda a mejorar nuestro código?
Escribe aquí tu respuesta

#### ¿Cómo se hace para aplicar inyección de dependencias de forma manual a un proyecto (sin utilizar librerías externas)?
Escribe aquí tu respuesta