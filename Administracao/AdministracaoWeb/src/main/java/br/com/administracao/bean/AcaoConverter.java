package br.com.administracao.bean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.administracao.model.Acoes;

@FacesConverter(value = "acaoConverter")
public class AcaoConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext facesContext,
			UIComponent uiComponent, String value) {
		if (value != null && !value.isEmpty()) {
			return (Acoes) uiComponent.getAttributes().get(value);
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext facesContext,
			UIComponent uiComponent, Object value) {
		if (value instanceof Acoes) {
			Acoes entity = (Acoes) value;
			if (entity != null && entity instanceof Acoes
					&& entity.getNro() != null) {
				uiComponent.getAttributes().put(entity.getNro().toString(),
						entity);
				return entity.getNro().toString();
			}
		}
		return "";
	}
}
