package br.com.acta.common.config.swagger.examples;

public final class SwaggerOpenapiDescriptions {
    public static final String ONBOARDING_CONTROLLER = """
            Endpoints responsáveis pelo cadastro de administradores e empresas.

            A API permite:

            - Verificar se a empresa já existe;
            - Solicitar acesso como administrador de uma empresa existente;
            - Criar uma empresa e seu primeiro administrador.
            """;

    public static final String AUTH_CONTROLLER = """
            Endpoints responsáveis pela autenticação e ativação de usuários.

            A API permite:

            - Consultar os dados do usuário autenticado;
            - Ativar um usuário a partir de uma identidade do Firebase.
            """;

    public static final String HEALTH_CONTROLLER = """
            Endpoint responsável por verificar a disponibilidade da API e do banco de dados.

            A verificação informa se a aplicação está disponível e se a conexão com o banco de dados está funcionando.
            """;
}