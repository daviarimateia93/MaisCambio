package br.com.maiscambio;

import br.com.maiscambio.model.entity.Usuario;

public @interface Perfil
{
	Usuario.Perfil[] value() default {};
}
