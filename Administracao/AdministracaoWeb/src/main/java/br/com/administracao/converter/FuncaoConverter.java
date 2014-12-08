package br.com.administracao.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.administracao.model.Funcoes;

@FacesConverter(value = "funcaoConverter")
public class FuncaoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext,
			UIComponent uiComponent, String value) {
		if (value != null && !value.isEmpty()) {
			return (Funcoes) uiComponent.getAttributes().get(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object value) {
		if (value instanceof Funcoes) {
			Funcoes entity = (Funcoes) value;
			if (entity != null && entity instanceof Funcoes
					&& entity.getHashCode() != null) {
				uiComponent.getAttributes().put(entity.getHashCode().toString(),
						entity);
				return entity.getHashCode().toString();
			}
		}
		return "";
	}
}
