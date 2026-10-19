package br.com.acta.common.config.swagger.examples;

public final class SwaggerResponseExamples {
    public static final String ERRO_400 = """
            {
              "mensagens": [
                "Um parâmetro obrigatório não foi informado"
              ],
              "httpStatus": 400,
              "timestamp": "2026-08-06T12:07:00"
            }
            """;

    public static final String ERRO_401 = """
            {
              "mensagens": [
                "ID Token do Firebase ausente ou inválido"
              ],
              "httpStatus": 401,
              "timestamp": "2026-08-06T12:07:00"
            }
            """;

    public static final String ERRO_403 = """
            {
              "mensagens": [
                "Usuário sem permissão para acessar o recurso"
              ],
              "httpStatus": 403,
              "timestamp": "2026-08-06T12:07:00"
            }
            """;

    public static final String ERRO_404 = """
            {
              "mensagens": [
                "O recurso solicitado não foi encontrado"
              ],
              "httpStatus": 404,
              "timestamp": "2026-08-06T12:07:00"
            }
            """;

    public static final String ERRO_409 = """
            {
              "mensagens": [
                "Não foi possível realizar a operação por conflito com os dados existentes"
              ],
              "httpStatus": 409,
              "timestamp": "2026-08-06T12:07:00"
            }
            """;

    public static final String ERRO_422 = """
            {
              "mensagens": [
                "Regra de negócio não atendida"
              ],
              "httpStatus": 422,
              "timestamp": "2026-08-06T12:07:00"
            }
            """;
}