.. code-block:: java
   :caption: Интерфейс, который надо реализовать

    public interface StepLocalization {

      /**
       * Makes translation by usage of a class.
       *
       * @param clz                       is a class whose metadata may be used for auxiliary purposes
       * @param template                  is a template of a string to be translated. It is usually taken
       *                                  from {@link Description} of a class. It may contain named parameters
       *                                  placed between '{}'. Names of parameters should correspond to class fields
       *                                  annotated by {@link DescriptionFragment}
       * @param descriptionTemplateParams is a list of parameters and their values. These parameters are taken from
       *                                  fields annotated by {@link DescriptionFragment}. This parameter is included
       *                                  because the list is possible to be re-used for some reasons.
       * @param locale                    is a used locale
       * @param <T>                       is a type of class
       * @return translated text
       */
      <T> String classTranslation(Class<T> clz,
                                  String template,
                                  List<TemplateParameter> descriptionTemplateParams,
                                  Locale locale);

      /**
       * Makes translation by usage of a method.
       *
       * @param method                    is a method whose metadata may be used for auxiliary purposes
       * @param template                  is a template of a string to be translated. It is usually taken
       *                                  from {@link Description} of a method. It may contain named parameters
       *                                  placed between '{}'. Names of parameters should correspond to
       *                                  method parameters annotated by {@link DescriptionFragment}. In case of methods
       *                                  numbers from 0 are allowed to be used as parameter names between '{}'. Each
       *                                  defined number should be an index of a parameter of the method signature.
       * @param descriptionTemplateParams is a list of parameters and their values. These parameters are taken from
       *                                  method parameters. Name of each {@link TemplateParameter} is formed by
       *                                  {@link DescriptionFragment} or index of the corresponding {@link Parameter} of
       *                                  the method signature. This parameter is included because the list is possible
       *                                  to be re-used for some reasons.
       * @param locale                    is a used locale
       * @return translated text
       */
      String methodTranslation(Method method,
                               String template,
                               List<TemplateParameter> descriptionTemplateParams,
                               Locale locale);

      /**
       * Makes translation by usage of other annotated element that differs from {@link Class} and {@link Method}
       *
       * @param member         is an annotated element whose metadata may be used for auxiliary purposes
       * @param toBeTranslated is a string to be translated. This string is usually taken from the member
       * @param locale         is a used locale
       * @param <T>            is a type of member
       * @return translated text
       */
      <T extends AnnotatedElement & Member> String memberTranslation(T member,
                                                                     String toBeTranslated,
                                                                     Locale locale);

      /**
       * Makes translation of a text.
       *
       * @param text   to be translated
       * @param locale is a used locale
       * @return translated text
       */
      String translation(String text, Locale locale);
    }

.. code-block:: properties
   :caption: Свойства которые следует заполнить

   DEFAULT_LOCALIZATION_ENGINE=org.my.project.MyLocalizationEngine
   DEFAULT_LOCALE=ru_RU