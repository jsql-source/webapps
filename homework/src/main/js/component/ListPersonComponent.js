import React, { Component } from 'react'
import ApiService from "../service/ApiService";

class ListPersonComponent extends Component {

    constructor(props) {
        super(props)
        this.state = {
            persons: [],            
            message: null,
            name: '', 
            amount: '',
        }   
        this.reloadPersonList = this.reloadPersonList.bind(this);
    }

    componentDidMount() {
        this.reloadPersonList();
    }

    reloadPersonList() {
        ApiService.getAll()
            .then((res) => {
                this.setState({persons: res.data.result})
            });
    }

    onDeleteAll = () => {
        ApiService.deleteAllPerson()
           .then(res => {
               this.setState({message : 'ОК'});
               this.setState({persons: []});
           }, (error) => {
        	   console.log(error);
           });

    }
    
    onRowChange = (e, personId) => {
        
		let changePerson = null;
		
		this.state.persons.map(person => {
			 if (person.id === personId) {
			 	person.amount = e.target.value;
			 	changePerson = person
			 	return;
			 }
		});
		
		if (!changePerson)
			return;
		
		ApiService.updatePerson(changePerson)
			.then((res) => {
				this.setState({persons: this.state.persons});
			});
		
    }
    
    onFormChange = (e) => {
        this.setState({ [e.target.name]: e.target.value });
    }
    
    onFormSubmit = (e) => {
        e.preventDefault();
       
        const { name, amount } = this.state;

        ApiService.addPerson({ name, amount})
	        .then((res) => {
	        	
	        	if (res.data.error)
	        		console.log(res.data.error);
	        		
	            this.reloadPersonList();
	        });
    }


    render() {
    	
    	const { name, amount } = this.state;    	

        return (
            <div class="person-comp">
            
            	<h4>Homework Application</h4>
            
                <div class="control-group">
	                <span class="control-label"></span>
	                
	                <div class="controls form-inline">	
	                	<form onSubmit={this.onFormSubmit}>	                    
		                    <input type="text" name="name" class="input-small" placeholder="Имя" id="inputName" onChange={this.onFormChange}  />
		                    <input type="text" name="amount" class="input-small" placeholder="Оклад" id="inputAmount" onChange={this.onFormChange} />
		                    	
		                    <button type="submit" class="btn btn-primary btn-sm">Добавить</button>		                   
                    	</form>	                    
	                </div>
            	</div>
            	
                <table class="table table-striped">
                    <thead>
                        <tr>                            
                            <th>Имя</th>
                            <th>Оклад</th>
                        </tr>
                    </thead>
                    <tbody>
                        {
                            this.state.persons.map(person =>
	                            <tr key={person.id}>
	                                <td>{person.name}</td>	
	                                <td><input type="text" name="amount" class="input-small" placeholder="Оклад" id="trInputAmount" onChange={(e) => this.onRowChange(e, person.id)} value={person.amount || ''}/></td>
	                                <td>	                                  
	                                    <button class="btn btn-success btn-sm" onClick={() => this.editPerson(person.id)} style={{marginLeft: '20px'}}>Сохранить</button>
	                                </td>
	                            </tr>
                            )
                        }
                    </tbody>
                </table>
                
                <button class="btn btn-danger btn-sm" onClick={this.onDeleteAll}>Удалить все</button>

            </div>
        );
    }

}

export default ListPersonComponent
