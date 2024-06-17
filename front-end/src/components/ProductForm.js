import React from 'react';


const ProductForm = ({ formData, setFormData, handleSubmit, isUpdate }) => {
    return (
        <form onSubmit={handleSubmit}>
            {isUpdate && (
                <div>
                    <label><strong>ID:</strong></label>
                    <input
                        type="text"
                        value={formData.id}
                        onChange={(e) => setFormData({ ...formData, id: e.target.value })}
                        disabled
                    />
                </div>
            )}
            <div>
                <label><strong>Name:</strong></label>
                <input
                    type="text"
                    value={formData.product}
                    onChange={(e) => setFormData({ ...formData, product: e.target.value })}
                    required
                />
            </div>
            <div>
                <label><strong>Category:</strong></label>
                <input
                    type="text"
                    value={formData.category}
                    onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                    required
                />
            </div>
            <div>
                <label><strong>Current Quantity:</strong></label>
                <input
                    type="text"
                    value={formData.curr_quantity}
                    onChange={(e) => setFormData({ ...formData, curr_quantity: e.target.value })}
                    required
                />
            </div>
            <div>
                <label><strong>Price:</strong></label>
                <input
                    type="text"
                    value={formData.price}
                    onChange={(e) => setFormData({ ...formData, price: e.target.value })}
                    required
                />
            </div>
            <div>
                <label><strong>Provider:</strong></label>
                <input
                    type="text"
                    value={formData.provider}
                    onChange={(e) => setFormData({ ...formData, provider: e.target.value })}
                    required
                />
            </div>
            <button type="submit">{isUpdate ? 'Update' : 'Add'}</button>
        </form>
    );
};

export default ProductForm;
