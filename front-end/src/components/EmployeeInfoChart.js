import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2';
import axios from 'axios';
import BaseUrl from './BaseUrl';

const EmployeeInfoChart = ({ employeeId }) => {
  const [employeeData, setEmployeeData] = useState([]);

  useEffect(() => {
    axios.get(BaseUrl() + `api/sales/${employeeId}`)
      .then(response => {
        const fetchedData = response.data;
        const chartData = fetchedData.reduce((acc, info) => {
          acc.labels.push(info.date);
          acc.data.push(info.value);
          return acc;
        }, { labels: [], data: [] });

        setEmployeeData(chartData);
        console.log(response.data);
      })
      .catch(error => console.error("Erro ao buscar dados", error));
  }, [employeeId]);

  const data = {
    labels: employeeData.labels,
    datasets: [
      {
        label: 'Informação do Empregado',
        data: employeeData.data,
        fill: false,
        borderColor: 'rgb(255, 99, 132)',
        tension: 0.1
      }
    ]
  };

  const options = {
    scales: {
      y: {
        beginAtZero: true
      }
    }
  };

  return <Line data={data} options={options} />;
};

export default EmployeeInfoChart;
