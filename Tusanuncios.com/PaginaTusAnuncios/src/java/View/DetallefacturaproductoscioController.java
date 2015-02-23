package View;

import Entities.Detallefacturaproductoscio;
import View.util.JsfUtil;
import View.util.JsfUtil.PersistAction;
import Model.DetallefacturaproductoscioFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@ManagedBean(name = "detallefacturaproductoscioController")
@SessionScoped
public class DetallefacturaproductoscioController implements Serializable {

    @EJB
    private Model.DetallefacturaproductoscioFacade ejbFacade;
    private List<Detallefacturaproductoscio> items = null;
    private Detallefacturaproductoscio selected;

    public DetallefacturaproductoscioController() {
    }

    public Detallefacturaproductoscio getSelected() {
        return selected;
    }

    public void setSelected(Detallefacturaproductoscio selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private DetallefacturaproductoscioFacade getFacade() {
        return ejbFacade;
    }

    public Detallefacturaproductoscio prepareCreate() {
        selected = new Detallefacturaproductoscio();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("DetallefacturaproductoscioCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("DetallefacturaproductoscioUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("DetallefacturaproductoscioDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Detallefacturaproductoscio> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<Detallefacturaproductoscio> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Detallefacturaproductoscio> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Detallefacturaproductoscio.class)
    public static class DetallefacturaproductoscioControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            DetallefacturaproductoscioController controller = (DetallefacturaproductoscioController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "detallefacturaproductoscioController");
            return controller.getFacade().find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Detallefacturaproductoscio) {
                Detallefacturaproductoscio o = (Detallefacturaproductoscio) object;
                return getStringKey(o.getIdDetalleFacturaProductoScio());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Detallefacturaproductoscio.class.getName()});
                return null;
            }
        }

    }

}
