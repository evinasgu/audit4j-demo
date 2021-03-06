package org.springframework.samples.petclinic.owner

import org.audit4j.core.AuditManager
import org.audit4j.core.annotation.Audit
import org.audit4j.core.annotation.IgnoreAudit
import org.audit4j.core.dto.AuditEvent
import org.audit4j.core.dto.Field
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid


@Controller
open class OwnerController(val owners: OwnerRepository) {

	val VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm"

    @InitBinder
    fun setAllowedFields(dataBinder: WebDataBinder) {
        dataBinder.setDisallowedFields("id")
	}

    @GetMapping("/owners/new")
    fun initCreationForm(model : MutableMap<String, Any>) : String {
        model.put("owner", Owner());
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping("/owners/new")
    fun processCreationForm(@Valid owner: Owner, result: BindingResult): String  {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
        } else {
            owners.save(owner);
            return "redirect:/owners/" + owner.id;
        }
    }

	
    @GetMapping("/owners/find")
    fun initFindForm(model: MutableMap<String, Any>) : String {
        model.put("owner", Owner());
        return "owners/findOwners";
    }

	@Audit
    @GetMapping("/owners")
    open fun processFindForm(owner: Owner, @IgnoreAudit result: BindingResult, @IgnoreAudit model : MutableMap<String, Any>) : String {

		//var manager = AuditManager.getInstance();
		//manager.audit(AuditEvent("the actor", "myMethod", Field("lastname", owner.lastName)))
        // find owners by last name
        val results = this.owners.findByLastName(owner.lastName);
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found");
            return "owners/findOwners";
        } else if (results.size == 1) {
            // 1 owner found
            return "redirect:/owners/" + results.first().id;
        } else {
            // multiple owners found
            model.put("selections", results);
            return "owners/ownersList";
        }
    }

    @GetMapping("/owners/{ownerId}/edit")
    fun initUpdateOwnerForm(@PathVariable("ownerId") ownerId: Int, model : Model) : String {
        val owner = owners.findById(ownerId)
        model.addAttribute(owner)
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
    }

    @PostMapping("/owners/{ownerId}/edit")
    fun processUpdateOwnerForm(@Valid owner : Owner, result : BindingResult, @PathVariable("ownerId") ownerId : Int) : String {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
        } else {
            owner.id =ownerId
            owners.save(owner);
            return "redirect:/owners/{ownerId}";
        }
    }

    /**
     * Custom handler for displaying an owner.
     *
     * @param ownerId the ID of the owner to display
     * @return a ModelMap with the model attributes for the view
     */
    @GetMapping("/owners/{ownerId}")
    fun showOwner(@PathVariable("ownerId") ownerId : Int, model: Model): String {
        model.addAttribute(this.owners.findById(ownerId))
        return "owners/ownerDetails"
}

}
