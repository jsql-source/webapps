import axios from 'axios';

const API_PERSON_BASE_URL = '/api/v1/person/';

class ApiService {

    getAll() {
        return axios.get(API_PERSON_BASE_URL + 'all');
    }

    deleteAllPerson() {
        return axios.delete(API_PERSON_BASE_URL + 'deleteall');
    }

    addPerson(person) {
        return axios.post(API_PERSON_BASE_URL + 'new', person);
    }

    updatePerson(person) {
        return axios.put(API_PERSON_BASE_URL + 'update', person);
    }

}

export default new ApiService();