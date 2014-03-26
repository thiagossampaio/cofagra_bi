jQuery(document).ready(function() {
	
	jQuery('.confirmRemocao').live('click',function(e){
		if(!confirm('Deseja realmente excluir?')){
			e.preventDefault();
			return false;
		}
	});
	
	jQuery('.setarFoco').focus();
	setarMascaras();
	
	try{
		color();
	}catch(e){
		
	}
	
});

function color(){
	
	var h = null;
	jQuery('.cores').ColorPicker({
	  onSubmit: function(hsb, hex, rgb, el) {
	     jQuery(el).val(hex);
	     jQuery(el).ColorPickerHide();
	  },
	  onBeforeShow: function () {
	    h = null;
	    jQuery(this).ColorPickerSetColor(this.value);
	  },
	  onChange: function (hsb, hex, rgb) {
	    h = hex + ",";
	  },
	  onHide: function (colpkr) {
		if(h != null){
			
			jQuery('.cores:first').val(jQuery('.cores:first').val() + h);
			jQuery('.cores:first').trigger("change");
			h = null;
		}
		jQuery(colpkr).fadeOut(500);
	    return false;
	  }
	  
	});
	
	jQuery('.cores').live('change',function(){
		if(jQuery(this).val() != null){
			
			jQuery('.ul-cores:first').html('');
			criarBlockCores();
			
		}
	});
}

function criarBlockCores(){
	
	jQuery.each(jQuery('.cores:first').val().split(';'), function(i,v){
		
		if(v != null && v != ""){
			var li = "<li style=\"background-color: #"+ v +";\"></li>";
			jQuery('.ul-cores:first').append(li);
			jQuery('.ul-cores:first').show();			
		}
		
	});
}

function setarMascaras(){
	jQuery('.moeda').maskMoney({showSymbol:true, symbol:"R$ ", decimal:",", thousands:"."});
}

function scrollToMessages(){
	primeScrollTo('msg', -100);
}

function primeScrollTo(idComponent, acrescimo) {
	try{
		var a = $(PrimeFaces.escapeClientId(idComponent)).offset();
		acrescimo = ((acrescimo == null) ? 0 : acrescimo);
		$("html,body").animate({
			scrollTop: (a.top + acrescimo),
			scrollLeft: a.left
		}, {
			easing: "easeInCirc"
		}, 1000);
	}catch(e){
		console.log(e);
	}
}

function alerta(texto){
	
	jQuery('#modalAlerta').find('#content').text(texto);
	jQuery('#modalAlerta').modal('show');
	
}

function handleTabChange(index){
	try{
		setaAbaAtivaCrud([{name:'param', value:index}]);
	}catch(e){
		console.log('catch');
	}
}

function limparAbasOcorrencias(index){
	try{
		if(index == 0){    
			limparOcorrencia();
			limparEntidade();
		}
	}catch(e){
	}
}

function handleTabChangeCall(index, fuc){
	
	try{
		if(fuc){
			fuc();
		}
	}catch(e){}
	
	handleTabChange(index);
	
}

function ajaxStatusOnComplete(){
	try{
		jQuery('.ajaxNavBar').hide();
		setarMascaras();
	}catch(e){}
}

function ajaxStatusOnStart(){
	try{
		jQuery('.ajaxNavBar').show();
	}catch(e){}
}

function monitorarDownload(b, a) {
    if (b) {
        b()
    }
    window.downloadMonitor = setInterval(function () {
        var c = PrimeFaces.getCookie("primefaces.download");
        if (c == "true") {
            if (a) {
                a()
            }
            clearInterval(window.downloadPoll);
            PrimeFaces.setCookie("primefaces.download", null)
        }
    }, 1000);
}

function handleSlideEnd(event, ui) {
//	ui.value = Current value of slider 
}
