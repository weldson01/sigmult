package models;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import enums.EstadoDaImpressao;
import enums.TipoImpressao;
import play.db.jpa.Model;

@Entity
public class Impressao extends Model{
	//public List<Arquivo> arquivos;
	@OneToOne(mappedBy="impressao")
	public Arquivo arquivo;
	
	public Calendar dataDeRecebimento;
	public String dataDeRecebimentoString;
	public Calendar dataDeSolicitacao;
	public String dataDeSolicitacaoString;
	//variável responsável por falar a quantidade de impressões que já foram realizadas
	public static Long impressos = (long) 0;
	public TipoImpressao tipo;
	public EstadoDaImpressao status;
	@ManyToOne
	@JoinColumn(name="Usuario_id")
	public Usuario usuarioImp;
	
	@ManyToOne
	@JoinColumn(name="Cota_id")
	public Cota cota;
}
