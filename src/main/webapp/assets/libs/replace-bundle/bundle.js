Utils.replace({
    usuario: {
	perfis: {
	    ADMIN: 'Administrador',
	    ESTABELECIMENTO_ESCRITA: 'Estabelecimento - Escrita',
	    ESTABELECIMENTO_LEITURA: 'Estabelecimento - Leitura',
	    ESTABELECIMENTO_DASHBOARD_LEITURA: 'Dashboard - Leitura',
	    ESTABELECIMENTO_TAXA_ESCRITA: 'Taxa - Escrita',
	    ESTABELECIMENTO_TAXA_LEITURA: 'Taxa - Leitura',
	    ESTABELECIMENTO_USUARIO_ESCRITA: 'Usuário - Escrita',
	    ESTABELECIMENTO_USUARIO_LEITURA: 'Usuário - Leitura'
	},
	status: {
	    ATIVO: 'Ativo',
	    INATIVO: 'Inativo'
	}
    },
    iof: {
	finalidade: {
	    COMPRA: 'Compra',
	    VENDA: 'Venda'
	}
    },
    taxa: {
	moeda: {
	    USD: 'Dólar Americano',
	    EUR: 'Euro',
	    GBP: 'Libras Esterlina',
	    CAD: 'Dólar Canadense',
	    AUD: 'Dólar Australiano',
	    CLP: 'Peso Chileno',
	    ARS: 'Peso Argentino',
	    CHF: 'Franco Suíço',
	    ZAR: 'Dólar Neozolandes',
	    MXN: 'Peso Mexicano',
	    UYU: 'Peso Uruguaio',
	    JPY: 'Iene',
	    NZD: 'Dólar Neozelandês'
	},
	finalidade: {
	    COMPRA: 'Compra',
	    VENDA: 'Venda'
	}
    },
    responses: {
	// AutenticacaoService
	USER_CREDENTIALS_NOT_FOUND: 'Crendenciais não encontrada',
	LICENCA_IS_EXPIRED: 'Licença expirada',
	INVALID_USER_CREDENTIALS_FORMAT: 'Formato inválido de credênciais',
	UNAUTHORIZED_USUARIO: 'Usuário não autorizado',
	YOU_DO_NOT_HAVE_PERMISSION: 'Você não tem permissão',
	USUARIO_IS_NOT_LOGGED_IN: 'Usuário não logado',
	USUARIO_NOT_FOUND: 'Usuário não encontrado',
	USUARIO_SENHA_DOES_NOT_MATCH: 'Senha inválido',
	USUARIO_STATUS_INATIVO: 'Usuário inativo',
	ESTABELECIMENTO_NOT_FOUND: 'Estabelecimento não encontrado',
	
	// EnderecoService
	ENDERECO_NOT_FOUND: 'Endereço não encontrado',
	ENDERECO_ALREADY_EXISTS: 'Endereço já existe',
	ENDERECO_CIDADE_MUST_NOT_BE_NULL: 'Cidade não deve ser nula',
	ENDERECO_CIDADE_ID_MUST_NOT_BE_NULL: 'Código da Cidade não deve ser nulo',
	ENDERECO_CIDADE_NOT_FOUND: 'Cidade não encontrada',
	ENDERECO_CIDADE_ESTADO_MUST_NOT_BE_NULL: 'Estado não deve ser nulo',
	ENDERECO_CIDADE_ESTADO_ID_MUST_NOT_BE_NULL: 'Código do Estado não deve ser nulo',
	ENDERECO_CIDADE_ESTADO_NOT_FOUND: 'Estado não encontrado',
	ENDERECO_CIDADE_ESTADO_PAIS_MUST_NOT_BE_NULL: 'Pais não deve ser nulo',
	ENDERECO_CIDADE_ESTADO_PAIS_ID_MUST_NOT_BE_NULL: 'Código do País não deve ser nulo',
	ENDERECO_CIDADE_ESTADO_PAIS_NOT_FOUND: 'País não encontrado',
	ENDERECO_LOGRADOURO_MUST_NOT_BE_EMPTY: 'Logradouro não deve ser vazio',
	ENDERECO_LOGRADOURO_MUST_NOT_BE_BIGGER_THAN_125_CHARACTERS: 'Logradouro não deve possuir mais de 125 caracteres',
	ENDERECO_COMPLEMENTO_MUST_NOT_BE_EMPTY: 'Complemento não deve ser vazio',
	ENDERECO_COMPLEMENTO_MUST_NOT_BE_BIGGER_THAN_45_CHARACTERS: 'Complemento não deve possuir mais de 45 caracteres',
	ENDERECO_NUMERO_MUST_NOT_BE_EMPTY: 'Número não deve ser vazio',
	ENDERECO_NUMERO_MUST_NOT_BE_BIGGER_THAN_20_CHARACTERS: 'Número não deve possuir mais de 20 caracteres',
	ENDERECO_CEP_MUST_NOT_BE_NULL: 'CEP não deve ser nulo',
	ENDERECO_CEP_MUST_BE_ONLY_NUMBERS_AND_8_CHARACTERS: 'CEP deve conter somente números e possuir 8 caracteres',
	ENDERECO_ENDERECO_ID_MUST_BE_NULL: 'Código deve ser nulo',
	ENDERECO_ENDERECO_ID_MUST_NOT_BE_NULL: 'Código não deve ser nulo',
	ENDERECO_MUST_NOT_BE_NULL: 'Endereço não deve ser nulo',
	
	// EstabelecimentoService
	ESTABELECIMENTO_MUST_NOT_BE_NULL: 'Estabelecimento não deve ser nulo',
	ESTABELECIMENTO_RAZAO_SOCIAL_MUST_BE_NULL: 'Razão social deve ser nula',
	ESTABELECIMENTO_RAZAO_SOCIAL_MUST_NOT_BE_EMPTY: 'Razão social não deve ser vazia',
	ESTABELECIMENTO_RAZAO_SOCIAL_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS: 'Razão social não deve possuir mais de 120 caracteres',
	ESTABELECIMENTO_NOME_FANTASIA_MUST_NOT_BE_EMPTY: 'Nome fantasia não deve ser vazia',
	ESTABELECIMENTO_NOME_FANTASIA_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS: 'Nome fantasia não deve possuir mais de 120 caracteres',
	ESTABELECIMENTO_MUST_HAVE_AT_LEAST_1_USUARIO: 'Deve haver pelo menos 1 usuário',
	ESTABELECIMENTO_EMAIL_MUST_NOT_BE_EMPTY: 'Email não deve ser vazio',
	ESTABELECIMENTO_NOT_FOUND: 'Estabelecimento não encontrado',
	USUARIO_DOES_NOT_HAS_PERMISSION: 'Usuário não tem permissão',
	ESTABELECIMENTO_ALREADY_EXISTS: 'Estabelecimento já existe',
	ESTABELECIMENTO_IS_ALREADY_IN_USE: 'Estabelecimento em uso',
	ESTABELECIMENTO_PAI_PESSOA_ID_MUST_NOT_BE_NULL: 'Código da Matriz não deve ser nulo',
	ESTABELECIMENTO_PAI_NOT_FOUND: 'Matriz não encontrada',
	ESTABELECIMENTO_PAI_PAI_MUST_BE_NULL: 'Estabelecimento Pai Pai deve ser nulo',
	ESTABELECIMENTO_NOME_FANTASIA_MUST_BE_THE_SAME_FROM_PAI: 'Nome fantasia deve ser o mesmo da Matriz',
	ESTABELECIMENTO_NOT_FOUND_OR_NOT_ABLE_TO_DO_THIS: 'Estabelecimento não encontrado',
	ESTABELECIMENTO_MUST_NOT_BE_THE_SAME_FROM_THE_LOGGED_ONE: 'Estabelecimento não deve ser o mesmo logado',
	ESTABELECIMENTO_TIME_ZONE_MUST_NOT_BE_EMPTY: 'TimeZone não deve ser vazio',
	ESTABELECIMENTO_TIME_ZONE_IS_INVALID: 'TimeZone inválido',
	
	// IofService
	IOF_MUST_NOT_BE_NULL: 'Iof não deve ser nulo',
	IOF_VALOR_ESPECIE_AND_VALOR_CARTAO_BOTH_MUST_NOT_BE_EMPTY_: 'Valor espécie ou Valor cartão, deve ser preenchido',
	IOF_VALOR_ESPECIE_MUST_BE_BIGGER_THAN_ZERO: 'Valor espécie deve ser maior que 0',
	IOF_VALOR_ESPECIE_TOO_LARGE: 'Valor espécie muito grande',
	IOF_VALOR_CARTAO_MUST_BE_BIGGER_THAN_ZERO: 'Valor cartão deve ser maior que 0',
	IOF_VALOR_CARTAO_TOO_LARGE: 'Valor cartão muito grande',
	IOF_DATA_MUST_NOT_BE_NULL: 'Data não deve ser nula',
	IOF_STATUS_MUST_NOT_BE_NULL: 'Status não deve ser nulo',
	IOF_FINALIDADE_MUST_NOT_BE_NULL: 'Finalidade não deve ser nula',
	IOF_IOF_ID_MUST_BE_NULL: 'Código deve ser nulo',
	
	// LicencaService
	LICENCA_IS_INVALID: 'Licença inválida',
	LICENCA_IS_EXPIRED: 'Licença expirada',
	ESTABELECIMENTO_NOT_FOUND: 'Estabelecimento não encontrado',
	USUARIO_DOES_NOT_BELONG_TO_ANY_ESTABELECIMENTO: 'Usuário não pertence a Estabelecimento',
	LICENCA_DOES_NOT_BELONG_TO_YOU: 'Licença não pertence a você',
	LICENCA_WAS_ALREADY_ACTIVATED: 'Licença já ativada',
	
	// PessoaService
	PESSOA_CNPJ_CPF_ID_ESTRANGEIRO_BOTH_MUST_NOT_BE_EMPTY: 'CNPJ ou CPF ou ID Estrangeiro, deve ser preenchido',
	PESSOA_CNPJ_CPF_ID_ESTRANGEIRO_BOTH_MUST_NOT_BE_FILLED: 'CNPJ ou CPF ou ID Estrangeiro, deve ser preenchido',
	PESSOA_CNPJ_MUST_BE_ONLY_NUMBERS_AND_14_CHARACTERS: 'CNPJ deve conter somente números e possuir 14 caracteres',
	PESSOA_CNPJ_IS_INVALID: 'CNPJ inválido',
	PESSOA_CPF_MUST_BE_ONLY_NUMBERS_AND_11_CHARACTERS: 'CPF deve conter somente números e possuir 11 caracteres',
	PESSOA_CPF_IS_INVALID: 'CPF inválido',
	PESSOA_ID_ESTRANGEIRO_IS_INVALID: 'ID Estrangeiro inválido',
	PESSOA_TELEFONE1_IS_INVALID: 'Telefone inválido',
	PESSOA_TELEFONE1_MUST_NOT_BE_EMPTY: 'Telefone não deve ser vazio',
	PESSOA_TELEFONE2_IS_INVALID: 'Telefone alternativo inválido',
	PESSOA_MUST_NOT_BE_NULL: 'Pessoa nao deve ser nula',
	PESSOA_PESSOA_ID_MUST_BE_NULL: 'Código deve ser nulo',
	PESSOA_PESSOA_ID_MUST_NOT_BE_NULL: 'Código não deve ser nulo',
	PESSOA_NOT_FOUND: 'Pessoa não encontrada',
	PESSOA_USUARIO_IS_ALREADY_IN_USE: 'Usuário já está em uso',
	PESSOA_ENDERECO_MUST_BELONGS_TO_PESSOA: 'Endereço deve pertencer à mesma Pessoa',
	PESSOA_EMAIL_MUST_NOT_BE_EMPTY: 'Email não deve ser vazio',
	PESSOA_EMAIL_MUST_NOT_BE_BIGGER_THAN_320_CHARACTERS: 'Email não deve possuir mais que 320 caracteres',
	PESSOA_EMAIL_IS_INVALID: 'Email inválido',
	PESSOA_NOME_CONTATO_MUST_NOT_BE_EMPTY: 'Nome de contato não deve ser vazio',
	PESSOA_NOME_CONTATO_MUST_NOT_BE_BIGGER_THAN_120_CHARACTERS: 'Nome de contato não deve possuir mais de 120 caracteres',
	PESSOA_CNPJ_MUST_NOT_BE_NULL: 'CNPJ não deve ser nulo',
	
	// TaxaService
	TAXA_MUST_NOT_BE_NULL: 'Taxa não deve ser nula',
	TAXA_ESTABELECIMENTO_MUST_NOT_BE_NULL: 'Estabelecimento não deve ser nulo',
	TAXA_ESTABELECIMENTO_PESSOA_ID_MUST_NOT_BE_NULL: 'Código do Estabelecimento não deve ser nulo',
	TAXA_ESTABELECIMENTO_NOT_FOUND: 'Estabelecimento não encontrado',
	TAXA_MOEDA_MUST_NOT_BE_NULL: 'Moeda não deve ser nula',
	TAXA_VALOR_ESPECIE_AND_VALOR_CARTAO_BOTH_MUST_NOT_BE_EMPTY_: 'Valor espécie ou Valor cartão, deve ser preenchido',
	TAXA_VALOR_ESPECIE_MUST_BE_BIGGER_THAN_ZERO: 'Valor espécie deve ser maior que 0',
	TAXA_VALOR_ESPECIE_TOO_LARGE: 'Valor espécie muito grande',
	TAXA_VALOR_CARTAO_MUST_BE_BIGGER_THAN_ZERO: 'Valor cartão deve ser maior que 0',
	TAXA_VALOR_CARTAO_TOO_LARGE: 'Valor cartão muito grande',
	TAXA_DATA_MUST_NOT_BE_NULL: 'Data não deve ser nula',
	TAXA_STATUS_MUST_NOT_BE_NULL: 'Status não deve ser nulo',
	TAXA_FINALIDADE_MUST_NOT_BE_NULL: 'Finalidade não deve ser nula',
	TAXA_TAXA_ID_MUST_BE_NULL: 'Código não deve ser nulo',
	
	// UsuarioService
	USUARIO_NOT_FOUND: 'Usuário não encontrado',
	USUARIO_OLD_SENHA_DOES_NOT_MATCH: 'Senha antiga inválida',
	USUARIO_MUST_NOT_BE_NULL: 'Usuário não deve ser nulo',
	USUARIO_USUARIO_ID_MUST_BE_NULL: 'Código deve ser nulo',
	USUARIO_USUARIO_ID_MUST_NOT_BE_NULL: 'Código não deve ser nulo',
	USUARIO_APELIDO_IS_ALREADY_IN_USE: 'Apelido em uso',
	USUARIO_APELIDO_MUST_NOT_BE_EMPTY: 'Apelido não deve ser vazio',
	USUARIO_APELIDO_MUST_NOT_BE_BIGGER_THAN_45_CHARACTERS: 'Apelido não deve possuir mais de 45 caracteres',
	USUARIO_APELIDO_MUST_CONTAINS_ONLY_LETTERS_NUMBERS_UNDERLINES_DASHES_AND_POINTS: 'Apelido deve conter apenas: Letras, Números, Underlines, Traços ou Pontos',
	USUARIO_SENHA_MUST_NOT_BE_EMPTY: 'Senha não deve ser vazia',
	USUARIO_SENHA_MUST_NOT_BE_BIGGER_THAN_128_CHARACTERS: 'Senha não deve possuir mais que 128 caracteres',
	USUARIO_STATUS_MUST_NOT_BE_NULL: 'Status não deve ser nulo',
	USUARIO_PESSOA_MUST_NOT_BE_NULL: 'Pessoa não deve ser nula',
	USUARIO_PESSOA_PESSOA_ID_MUST_NOT_BE_NULL: 'Código da Pessoa não deve ser nulo',
	USUARIO_PESSOA_NOT_FOUND: 'Pessoa não encontrada',
	USUARIO_MUST_NOT_BE_THE_SAME_FROM_THE_LOGGED_ONE: 'Usuário não deve ser o mesmo logado',
	USUARIO_MUST_NOT_CONTAINS_DUPLICATED_PERFIS: 'Não deve conter perfis duplicados',
	USUARIO_PESSOA_MUST_BE_ESTABELECIMENTO: 'Pessoa deve ser um Estabelecimento',
	USUARIO_PESSOA_INVALID: 'Pessoa inválida',
	USUARIO_MUST_NOT_HAS_ADMIN_PERFIL: 'Usuário não deve conter perfil ADMIN'
    }
});